package org.netkernel.lang.kotlin;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.netkernel.layer0.nkf.INKFRequestContext;
import org.netkernel.layer0.nkf.NKFException;
import org.netkernel.module.standard.endpoint.StandardTransreptorImpl;

public class KotlinScriptCompiler extends StandardTransreptorImpl
{
	private ClassLoader mClassLoader;
	
	public KotlinScriptCompiler()
	{
		this.declareThreadSafe();
		this.declareToRepresentation(KotlinScriptRepresentation.class);
	}
	
	@Override
	public void postCommission(INKFRequestContext aContext) throws Exception
	{
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
		mClassLoader=new URLClassLoader(urla);
	}

	@Override
	public void onTransrept(INKFRequestContext aContext) throws Exception
	{
		String script=aContext.sourcePrimary(String.class);
		
		Thread.currentThread().setContextClassLoader(mClassLoader);
		ScriptEngineManager sem=new ScriptEngineManager(mClassLoader);
		ScriptEngineFactory sef=(ScriptEngineFactory)mClassLoader.loadClass("org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory").newInstance();
		sem.registerEngineExtension("kts", sef);
		ScriptEngine engine = sem.getEngineByExtension("kts");
		Compilable c=(Compilable)engine;
		CompiledScript compiled=c.compile(script);
		KotlinScriptRepresentation rep=new KotlinScriptRepresentation(compiled);
		aContext.createResponseFrom(rep);
	}
	

	
}
