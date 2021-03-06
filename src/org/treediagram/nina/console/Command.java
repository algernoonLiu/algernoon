/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treediagram.nina.console;

import org.treediagram.nina.console.exception.CommandException;

/**
 * 控制台命令接口
 * 
 * @author hy
 */
public interface Command {
	/**
	 * 获取命令名
	 */
	String name();

	/**
	 * 获取命令的描述信息
	 */
	String description();

	/**
	 * 执行控制台指令
	 * 
	 * @param arguments 命令参数
	 * @throws CommandException 执行失败时抛出
	 */
	boolean execute(String[] arguments) throws CommandException;
}
