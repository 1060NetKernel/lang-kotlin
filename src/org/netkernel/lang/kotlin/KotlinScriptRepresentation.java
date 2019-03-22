package org.netkernel.lang.kotlin;

import javax.script.CompiledScript;

public class KotlinScriptRepresentation
{
	private final CompiledScript mScript;
	public KotlinScriptRepresentation(CompiledScript aScript)
	{	mScript=aScript;
	}
	public CompiledScript getScript()
	{	return mScript;
	}
}
