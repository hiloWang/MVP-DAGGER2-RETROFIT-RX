/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hilo.domain.repository;

import com.github.hilo.domain.User;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link User} related data.
 * Repository是DDD中的概念，强调Repository是受Domain驱动的，Repository中定义的功能要体现Domain的意图和约束
 * 使用Repository，隐含着一种意图倾向，就是 Domain需要什么我才提供什么，不该提供的功能就不要提供，一切都是以Domain的需求为核心；
 * <p>
 * 主要策略是通过一个工厂根据一定的条件选取不同的数据来源。
 * 比如，通过ID获取一个用户时，如果这个用户在缓存中已经存在，则硬盘缓存数据源会被选中，否则会通过向云端发起请求获取数据，然后存储到硬盘缓存。
 * 这一切背后的原理是由于原始数据对于客户端是透明的，客户端并不关心数据是来源于内存、硬盘还是云端，它需要关心的是数据可以正确地获取到
 */
public interface UserRepository {
	/**
	 * Get an {@link rx.Observable} which will emit a List of {@link User}.
	 */
	Observable<List<User>> users();

	/**
	 * Get an {@link rx.Observable} which will emit a {@link User}.
	 *
	 * @param userId The user id used to retrieve user data.
	 */
	Observable<User> user(final int userId);

	Observable<User> user();
}
