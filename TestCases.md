# SimpleChat Test Cases

# Testcase 2001 – Server startup check with default arguments

**Instructions:**
- Start the server program.

**Expected Results:**
- The server reports that it is listening for clients by displaying: 
`Server listening for clients on port 5555`.
- The server console waits for user input.

**Cleanup:** Terminate the server program.

Result: **PASS** | Fail 

---

## Testcase 2002 – Client startup check without a login

**Instructions:**
- Start the Client program without specifying the loginID as an argument.

**Expected Results:**
- The client reports it cannot connect without a login by displaying: 
`ERROR - No login ID specified. Connection aborted.`
- The client terminates.

**Cleanup:** Terminate the client program if still active.

Result: **PASS** | Fail 

---

## Testcase 2003 – Client startup check with a login and without a server

**Instructions:**
- Start the Client program while specifying loginID as an argument.

**Expected Results:**
- The client reports it cannot connect to a server by displaying: `ERROR - Can't setup connection! Terminating client.`
- The client terminates.

**Cleanup:** Terminate the client program if still active.

Result: **PASS** | Fail 

---

## Testcase 2004: Client connection with default arguments

**Instructions:**
- Start a server
- Start a client using loginID

**Expected Result:**
- The server displays:
- - `A new client has connected to the server.`
- - `Message received: #login <loginID> from null.`
- - `<loginID> has logged on.`
- The client displays: `<loginID> has logged on.`
- Both wait for input

**Cleanup:** Terminate client and server.

Result: **PASS** | Fail 

---

## Testcase 2005: Client Data transfer and data echo

**Instructions:**
- Start a server and a client using default arguments
- Type a message in the client console and press ENTER

**Expected Result:**
- Client message is echoed prefixed with `<loginID>`
- Server shows: `Message received: <user input> from <loginID>`

**Cleanup:** Terminate both programs.

Result: **PASS** | Fail 

---

## Testcase 2006: Multiple local connections

**Instructions:**
- Start a server and multiple clients with different loginIDs
- Type messages on all clients and server consoles

**Expected Result:**
- Client messages are echoed as in Testcase 2005
- Server messages echoed to all clients prefixed with `SERVER MSG>`

**Cleanup:** Terminate clients and server.

Result: **PASS** | Fail 

---

## Testcase 2007: Server termination command check

**Instructions:**
- Start a server and type `#quit` in the server console

**Expected Result:**
- The server terminates gracefully

**Cleanup:** Terminate server if still active.

Result: **PASS** | Fail 

---

## Testcase 2008: Server close command check

**Instructions:**
- Start server and client
- Stop server using `#stop`, then `#close`

**Expected Result:**
- Server stops listening and disconnects clients
- Client sees shutdown and terminates

**Cleanup:** Terminate both programs.

Result: **PASS** | Fail 

---

## Testcase 2009: Server restart

**Instructions:**
- Start server, close with `#close`, restart with `#start`
- Connect a client

**Expected Result:**
- Server displays restart message
- Client connects normally

**Cleanup:** Terminate client and server.

Result: **PASS** | Fail 

---

## Testcase 2010: Client termination command check

**Instructions:**
- Start server and client
- Type `#quit` in client console

**Expected Result:**
- Client terminates gracefully

**Cleanup:** Terminate client if still active.

Result: **PASS** | Fail 

---

## Testcase 2011: Client logoff check

**Instructions:**
- Start server and connect a client
- Type `#logoff` in client console

**Expected Result:**
- Client disconnects and displays `Connection closed`

**Cleanup:** Terminate client and/or server.

Result: **PASS** | Fail 

---

## Testcase 2012: Starting a server on a non-default port

**Instructions:**
- Start a server with port 1234

**Expected Result:**
- Server displays: `Server listening for connections on port 1234`

**Cleanup:** Terminate server with `#quit`.

Result: **PASS** | Fail 

---

## Testcase 2013: Connecting a client to a non-default port

**Instructions:**
- Start a server on port 1234
- Start a client with `<loginID> <host> 1234`

**Expected Result:**
- Connection occurs normally

**Cleanup:** Terminate both programs.

Result: **PASS** | Fail 

---
