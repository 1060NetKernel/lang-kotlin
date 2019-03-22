package org.netkernel.lang.kotlin.test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.netkernel.layer0.nkf.INKFRequestContext;
import org.netkernel.layer0.nkf.NKFException;
import org.netkernel.layer0.util.DynamicURLClassLoader;
import org.netkernel.layer0.util.SpaceClassLoader;
import org.netkernel.module.standard.endpoint.StandardAccessorImpl;

public class TestAccessor extends StandardAccessorImpl
{
	public void onSource(INKFRequestContext aContext) throws Exception
	{
		// Mock up URLClassloader as NetKernel classloader causes problems that
		// I haven't managed to work out yet - get error:
		// javax.script.ScriptException
		// Error: error: cannot access script base class 'org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate'.
		// Check your module classpath for missing or conflicting dependencies
		
		URI sourceURI=getStandardSpace().getOwningModule().getSource();
		String source=sourceURI.toString();
		//System.out.println(source);
		List<URL> urls=new ArrayList();
		if (source.startsWith("file:") && source.endsWith("/"))
		{	File f = new File(sourceURI);
			File libDir=new File(f, "lib");
			File[] libs=libDir.listFiles();
			for (File lib : libs)
			{	System.out.println(lib);
				urls.add(lib.toURL());
			}
		}
		else
		{	throw new NKFException("cannot support jar module yet");
		}
		URL[] urla=new URL[urls.size()];
		urla=urls.toArray(urla);
		ClassLoader cl=new URLClassLoader(urla);
		
		//ClassLoader cl=this.getClass().getClassLoader();
		
		//tried other classloaders too with no success
		//ArrayList l=new ArrayList<>();
		//for (int i=0; i<urls.length; i++)
		//{	l.add("jar:"+urls[i].toString()+"!/");
		//}
		//ClassLoader cl=new DynamicURLClassLoader(l);
		//ClassLoader cl=new SpaceClassLoader(this.getClass().getClassLoader(), l, Collections.EMPTY_LIST, "");
		
		// Compile script
		Thread.currentThread().setContextClassLoader(cl);
		ScriptEngineManager sem=new ScriptEngineManager(cl);
		ScriptEngineFactory sef=(ScriptEngineFactory)cl.loadClass("org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory").newInstance();
		sem.registerEngineExtension("kts", sef);
		ScriptEngine engine = sem.getEngineByExtension("kts");
		Compilable c=(Compilable)engine;
		String script="2+2";
		CompiledScript compiled=c.compile(script);
		
		// Execute script
		ScriptContext ctx=new SimpleScriptContext();
		Object result=compiled.eval(ctx);
		System.out.println("result :"+result);
		
		
	}
}
