package org.acme.filters;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Conditional Filters with Name Binding: Only When You Say So
Sometimes, you only want special handling for specific endpoints—like audit logging, authentication, or a “don’t embarrass me in production” sanity check.
That’s where Name Binding comes in.
*/
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Audited {
}
