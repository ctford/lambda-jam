There are two empty exercise projects: `robozzle` and `life` in
subdirectories of the same name. Three worked examples are in the
`worked-examples` surdirectory.

Before starting the exercises, see `INSTALL.txt`.

After starting the exercises, you can refer to the user guide at
<https://github.com/marick/Midje/wiki>. If the network is down, the
selected pages in `user-guide` may be helpful.

=== STARTING AN EXERCISE WITH AUTOTEST

    $ cd robozzle
    $ lein repl
    nREPL server started on port 53118
    ...
    user=> (use 'midje.repl)
    Run `(doc midje)` for Midje usage.
    Run `(doc midje-repl)` for descriptions of Midje repl functions.
    nil
    user=> (autotest)
    
    ======================================================================
    Loading (robozzle.core robozzle.t-core)
    No facts were checked. Is that what you wanted?
    true
    user=>

Note: `autotest` currently overlooks files that throw an error while
being loaded. So it's wise to start it before you start writing
code. Errors made *after* autotest starts are no problem. Autotest
will load them and tell you about the error:

    ======================================================================
    Loading (robozzle.core robozzle.t-core)
    LOAD FAILURE for robozzle.core
    java.lang.RuntimeException: Unable to resolve symbol: skl in this context, compiling:(robozzle/core.clj:0:0)
    The exception has been stored in #'*me, so `(pst *me)` will show the stack trace.
    FAILURE: 1 check failed. 

Each exercise has a `core.clj` file under `src` and a `t_core.clj`
file under `test`. They are set up so that you can put the facts in
either, depending on your preference.
