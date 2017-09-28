package com.orpc.server.annotion;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

@Retention(RUNTIME)
@Target(TYPE)
@Service
public @interface RpcService {
	Class<?> value();
}
