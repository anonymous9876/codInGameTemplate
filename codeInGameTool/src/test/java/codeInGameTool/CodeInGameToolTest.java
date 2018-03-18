package codeInGameTool;

import org.junit.Test;

import junit.framework.TestCase;


public class CodeInGameToolTest extends TestCase {
	
	@Test
	public void testPackageToImportPattern() {
		assertEquals("import com.test", CodeInGameTool.packageToImportPatternWithoutSemicolon("package com.test;"));
	}
}
