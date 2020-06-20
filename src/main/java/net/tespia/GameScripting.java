package net.tespia;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameScripting {

    private final ScriptEngine engine;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);


    public GameScripting(Map<String, Object> context, String fileName) {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            context.forEach(this.engine::put);
            this.engine.eval(new FileReader(fileName));
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getStartPortal() {
        return (String) this.engine.get("startPortal");
    }

    public String getStrVar(String var) {
        return (String) this.engine.get(var);
    }

    public void callEvent(String eventName, Object... args) {
        Invocable invocable = (Invocable) engine;
        try {
            invocable.invokeFunction(eventName, args);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Integer getIntVar(String var) {
        return (Integer) this.engine.get(var);
    }

    public void addBinding(String key, Object o) {
        this.engine.put(key, o);
    }
}
