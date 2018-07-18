# Red Hat - 3scale HTTP Log Monitor

## Rules

* Add a README.md file documenting what you did and how you'd improve on.
* Send back the code **including the .git directory**.
* We encourage you to implement this exercise in Ruby, Go or Rust, but we will
  also accept implementations in Python, C/C++ and Java. Please drop us a line
  in case you feel none of these options works well for you.
* You are not allowed to use external libraries unless you can justify the need
  and ask us for an exception. If you have doubts, get in touch. As a rule of
  thumb, the **only** external libraries you are allowed to use are:
  * Libraries provided with a base installation of the language you use.
  * Regular expression libraries.
  * JSON libraries for encoding output messages.
  * The system's C library and any means to access it.
* You are *not allowed* to use or spawn external programs.
* Adding a test suite is explicitly out of scope and not required nor evaluated.

## Description

We want to build a log monitor for an HTTP access.log file that receives data
generated from multiple servers running a web application.

Create a console program that is invoked with the following arguments:

```
logmonitor <access.log> <traffic_threshold>
```

where *access.log* is the path to a file that is being **actively** written-to and
*traffic_threshold* is an integer parameter described in the requirements below.
The task of this tool is to monitor the specified log file for HTTP traffic with
log lines like these:

```
194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] "GET /some/path?param1=x&param2=y HTTP/1.1" 200 0.006065388
213.1.20.7 [13/02/2016 16:45:02] "POST /some/other/path HTTP/1.0" 201 0.012901348
```

Conformant log lines contain US ASCII characters only, and the fields are as
follows, from left to right, always separated by one or more space or
non-printable characters:
  * IPv4 addresses, from left to right, are the client's IP address, followed
    by optional additional addresses with the list of proxies forwarding the
    request to each other until the last one reaches us. The whole list of
    addresses, including the origin, are referred to as the proxy chain.
    Addresses are separated by commas (`,`) and any amount of space or
    non-printable characters.
  * The date and the time of the request in UTC as recorded by the server that
    processed it, enclosed in brackets, and separated by a single space
    character.
  * The request information enclosed in double quotes, with the method, the path
    including any URL parameters, and the protocol and its version, each
    separated by one space character.
  * The status code of the response.
  * The seconds it took the server to process and respond to the request.

All output generated by the monitoring tool should be in JSON format, and all
messages contain at least two mandatory fields:

  * `timestamp`: a UNIX timestamp to be filled in with the current time when the
    message is output, represented as an integer.
  * `message_type`: a string with the message type as defined in each
    requirement.

## Application Requirements

### 1 - Stats and alerts

#### Stats

* Once started the program must run continuously. Every 10 seconds *and* when
  terminated it should output a message containing:
  * The number of GET and POST methods hits, as well as the total number of
    hits. Use the keys `get`, `post` and `hits` respectively.
  * The number of requests that have been proxied. Use the key `forwarded_hits`.
  * The proxy that forwarded most requests and number of forwarded requests. Use
    the keys `most_used_proxy` and `most_used_proxy_hits` respectively.
  * The 95 percentile request time. Use the key `p95`.
  * The number of non-conformant log lines. Use the key `bad_lines`.

* Format
  * The message should adhere to the JSON format below:

```json
{ "timestamp": <timestamp>, "message_type": "stats",
  "get": <integer>, "post": <integer>, "hits": <integer>,
  "forwarded_hits": <integer>,
  "most_used_proxy": <string>,
  "most_used_proxy_hits": <integer>,
  "p95": <float>,
  "bad_lines": <integer> }
```

#### Alerts

* Whenever the conditions below are detected, output a message describing an
  alert. This message should use the key `message_type` with a value of `alert`
  and another key `alert_type` with a variable value depending on the alert as
  specified below.

##### Traffic threshold crossed alert

* Whenever total traffic for the past minute _crosses above_ or _crosses below_ the
  traffic threshold *traffic_threshold*, output a message with the JSON format
  below:

```json
{ "timestamp": <timestamp>, "message_type": "alert",
  "alert_type": <alert_type>,
  "period": <period>,
  "threshold": <integer>,
  "current_value": <integer> }
```

The new keys are defined as follows:

  * `alert_type`: A string specifying the type of alert. For this section it can
    be either `traffic_above_threshold` or `traffic_below_threshold` depending
    on whether the cross happened upwards or downwards of the threshold,
    respectively.
  * `period`: The relative period to which the alert applies. In both cases its
    value should be the string `minute` for these alerts.
  * `threshold`: The *traffic_threshold* value given as argument to our monitor.
  * `current_value`: The amount of requests performed in the relative period (in
    this case, the past minute).

> __Note__: *Past minute* is defined as the previous 60 seconds at **any** given time.

### 2 - Efficient proxy chains

* Add another alert to your program. Whenever an inefficient proxy chain is
  seen, based on proxy chains already observed, output a message with the JSON
  format below:

```json
{ "timestamp": <timestamp>, "message_type": "alert",
  "alert_type": "ok",
  "proxy_chain": <list of addresses in the proxy chain>,
  "inefficient_addresses": <list of addresses with more proxies than needed>,
  "efficient_proxy_chains": <list of efficient proxy chains> }
```

#### Definitions

  An _address_ or an _IP address_ is a string representing a valid IPv4 address
  and is always associated to a client, a server or a proxy.

  A _client_ is a computer performing a request to our web application. Client
  addresses always appear as the first address in our log lines and so are the
  first address of any proxy chain. They are also referred to as _origin_
  addresses.

  A _server_ is a computer running an instance of our web application. Server
  addresses are internal and never appear in our log lines.

  A _proxy_ is a computer forwarding requests from a client or another proxy to
  a server or to another proxy.

  A _proxy chain_ is an ordered list of addresses composed of a client IP address
  and zero or more proxy addresses, so that they describe the path a request
  follows from the client address to one of our servers, with the last address
  in the chain connecting directly to our pool of server addresses.

  An _inefficient proxy chain_ is one that contains **at least one** address common
  to other observed proxy chains and a number of proxies following the common
  address larger than needed to reach our servers if the request would have been
  forwarded differently.

  An _efficient proxy chain_ is one for which **all** addresses are followed by the
  minimum amount of proxies possible to reach our pool of server addresses as
  per other observed proxy chains.

  The `<list of addresses in the proxy chain>` above should contain the
  referenced proxy chain's addresses. Note that all proxy chains should specify
  their addresses in the order in which they appear defined by the proxy chain.

  The `<list of addresses with more proxies than needed>` above is the list of
  addresses in the proxy chain that generated the alert that are followed by
  more proxies than needed to reach our pool of server addresses, in no
  particular order.

  The `<list of efficient proxy chains>` above is the list of unique proxy
  chains that we can generate sharing the same origin address as the inefficient
  proxy chain but taking different forwarding proxies so the proxy chain becomes
  efficient, in no particular order.

#### Example

  Check out the following example (non significant fields omitted):

  ```
  100.1.10.11, 89.2.1.13, 64.200.1.20, 10.1.10.16 * OK *
  80.139.20.2, 190.9.11.1, 89.2.1.13, 64.200.1.20, 10.1.10.15 * OK *
  193.176.1.4, 64.200.1.20, 10.1.10.15 * OK *
  77.8.100.30, 109.1.20.3, 64.200.1.20, 68.35.10.101, 10.1.10.13 * ALERT *!
  ```

  Assuming this is the only input ever seen, this would have output the
  following JSON message:

```json
{ "timestamp": 123456789, "message_type": "alert",
  "alert_type": "inefficient_proxy_chain",
  "proxy_chain": ["77.8.100.30", "109.1.20.3", "64.200.1.20",
                  "68.35.10.101", "10.1.10.13"],
  "inefficient_addresses": ["64.200.1.20"],
  "efficient_proxy_chains": [
    ["77.8.100.30", "109.1.20.3", "64.200.1.20", "10.1.10.16"],
    ["77.8.100.30", "109.1.20.3", "64.200.1.20", "10.1.10.15"] ] }
```

### 3 - Optimization

* Pay attention to the CPU and memory usage of your solution. Is the CPU running
  at 100% all the time when you execute your program? What information are you
  storing to solve the inefficient proxy chain problem? We ask you to spend some
  time trying to optimize your solution (if you have not already done so), and
  documenting your decisions.

## Notes

* This is an application working on real time data arriving from several dozen
  server machines whose clocks may not be in sync at all.
* Any proxy that can connect directly to a server running our web application
  can connect to any server in our pool of servers, so that the particular
  server that processes each request does not matter for connectivity purposes.
* The code is expected to run exclusively on fairly modern Linux servers. It is
  fine and encouraged to make use of Linux-specific features.
* We expect your deliverable to document requirements and full instructions on
  how to run it and how to build any artifact that needs to be built. Please do
  NOT include code binaries of any kind - any and all such binaries, where
  applicable, should be built from source code and will be built by us.

## Evaluation

We will pay close attention to:

* Correct functionality and error handling.
* Code quality, readability and design.
* Choice and usage of data structures and algorithms.
* Performance, and efficient usage of CPU and memory resources.
* Accuracy of data and latency of stats and alerts (ie. is output data correct?
  does a request that should trigger an alert do so immediately?)
* Documentation and justification of your decisions.
* Instructions on how to run and (where applicable) build your exercise.
* git history as if you were submitting a pull request to a maintainer. Please
  use the .git repository that should have been already provided along with this
  file to keep track of your changes.
