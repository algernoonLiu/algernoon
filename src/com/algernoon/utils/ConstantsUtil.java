package com.algernoon.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConstantsUtil extends TagSupport{
	
	private static final long serialVersionUID = -1870274338451151380L;
	
	private final Log log = LogFactory.getLog(ConstantsUtil.class);
	
	public String clazz = ConstantsUtil.class.getName();
   
	protected String scope = null;
    
	protected String var = null;
	
	@Override
	public int doStartTag() throws JspException {
		Class c = null;
        int toScope = PageContext.PAGE_SCOPE;
        if (scope != null) {
            toScope = getScope(scope);
        }
        try {
            c = Class.forName(clazz);
        } catch (ClassNotFoundException cnf) {
           log.error("ClassNotFound - maybe a typo?");
            throw new JspException(cnf.getMessage());
        }

       try {
            if (var == null || var.length() == 0) {
                throw new JspException("常量参数var必须填写！");
            } else {
                try {
                    Object value = c.getField(var).get(this);
                    pageContext.setAttribute(c.getField(var).getName(), value, toScope);
                } catch (NoSuchFieldException nsf) {
                    log.error(nsf.getMessage());
                    throw new JspException(nsf);
                }
            }
         } catch (IllegalAccessException iae) {
            log.error("Illegal Access Exception - maybe a classloader issue?");
            throw new JspException(iae);
        }
     return (SKIP_BODY);
	}
	
	public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return (this.scope);
    }
    
    public void setVar(String var) {
        this.var = var;
     }

    public String getVar() {
        return (this.var);
    }
    
    @Override
    public void release() {
    	super.release();
        clazz = null;
        scope = ConstantsUtil.class.getName();
    }
    
    private static final Map scopes = new HashMap();
    static {
        scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
        scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
        scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
        scopes.put("application", new Integer(PageContext.APPLICATION_SCOPE));
    }
    
    public int getScope(String scopeName) throws JspException {
        Integer scope = (Integer) scopes.get(scopeName.toLowerCase());

    if (scope == null) {
        throw new JspException("Scope '" + scopeName + "' not a valid option");
    }

    return scope.intValue();
 }
}
