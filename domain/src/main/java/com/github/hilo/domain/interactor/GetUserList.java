/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
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
package com.github.hilo.domain.interactor;

import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;
import com.github.hilo.domain.repository.UserRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all
 */

public class GetUserList extends UseCase {

  private final UserRepository userRepository;

  @Inject
  public GetUserList(UserRepository userRepository, ThreadExecutor threadExecutor,
                     PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.userRepository = userRepository;
  }

  @Override
  protected Observable buildUseCaseObservable() {
    // 当调用父类UseCase.execute()方法时，触发本方法，并指向子类的实现；
    return userRepository.user(); // UserRepository是数据层实现的，当调用users()方法时，会调用data层的实现，并返回给调用该方法的人，主要用于获取data层数据并转换成User类型
  }
}
