/**
 * Copyright 2015 David Karnok and Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package hu.akarnokd.rxjava2.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;

import org.reactivestreams.*;

import hu.akarnokd.rxjava2.disposables.Disposable;
import hu.akarnokd.rxjava2.functions.Consumer;
import hu.akarnokd.rxjava2.observables.ConnectableObservable;

/**
 * Wraps a ConnectableObservable and calls its connect() method once
 * the specified number of Subscribers have subscribed.
 *
 * @param <T> the value type of the chain
 */
public final class PublisherAutoConnect<T> implements Publisher<T> {
    final ConnectableObservable<? extends T> source;
    final int numberOfSubscribers;
    final Consumer<? super Disposable> connection;
    final AtomicInteger clients;
    
    public PublisherAutoConnect(ConnectableObservable<? extends T> source,
            int numberOfSubscribers,
            Consumer<? super Disposable> connection) {
        if (numberOfSubscribers <= 0) {
            throw new IllegalArgumentException("numberOfSubscribers > 0 required");
        }
        this.source = source;
        this.numberOfSubscribers = numberOfSubscribers;
        this.connection = connection;
        this.clients = new AtomicInteger();
    }
    
    @Override
    public void subscribe(Subscriber<? super T> child) {
        source.unsafeSubscribe(child);
        if (clients.incrementAndGet() == numberOfSubscribers) {
            source.connect(connection);
        }
    }
}
