package net.tespia.scripting;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;

public class ScriptTask implements Runnable {

    private ScriptEngine engine;
    private String script;
    private ClientScript clientScript;

    public ScriptTask(ScriptEngine engine, String script, ClientScript clientScript) {
        this.engine = engine;
        this.script = script;
        this.clientScript = clientScript;
    }

    public void initiate() {
        ScriptContext context = this.engine.getContext();

    }

    @Override
    public void run() {
        initiate();
    }
}
