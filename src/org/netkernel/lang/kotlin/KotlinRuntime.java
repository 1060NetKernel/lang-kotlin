package org.netkernel.lang.kotlin;

import javax.script.ScriptContext;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.netkernel.layer0.nkf.INKFRequestContext;
import org.netkernel.module.standard.endpoint.StandardAccessorImpl;

public class KotlinRuntime extends StandardAccessorImpl
{
	public KotlinRuntime()
	{
		this.declareThreadSafe();
	}
	
	public void onSource(INKFRequestContext aContext) throws Exception
	{
		KotlinScriptRepresentation script=aContext.source("arg:operator",KotlinScriptRepresentation.class);
		ScriptContext ctx=new SimpleScriptContext();
		SimpleBindings bindings=new SimpleBindings();
		bindings.put("context", aContext);
		ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		ctx.setAttribute("context", aContext, ScriptContext.ENGINE_SCOPE);
		Object result=script.getScript().eval(ctx);
		aContext.createResponseFrom(result);
	}
}
