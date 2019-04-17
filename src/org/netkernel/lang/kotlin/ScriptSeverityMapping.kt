package org.netkernel.lang.kotlin

import org.netkernel.layer0.nkf.INKFLocale
import kotlin.script.experimental.api.ScriptDiagnostic

internal fun ScriptDiagnostic.Severity.getNKFLogLevel(): Int {
    return when(this) {
        ScriptDiagnostic.Severity.FATAL -> INKFLocale.LEVEL_SEVERE
        ScriptDiagnostic.Severity.ERROR -> INKFLocale.LEVEL_SEVERE
        ScriptDiagnostic.Severity.WARNING -> INKFLocale.LEVEL_WARNING
        ScriptDiagnostic.Severity.INFO -> INKFLocale.LEVEL_INFO
        ScriptDiagnostic.Severity.DEBUG -> INKFLocale.LEVEL_DEBUG
    }
}
