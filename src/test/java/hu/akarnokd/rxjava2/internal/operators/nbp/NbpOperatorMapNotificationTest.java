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

package hu.akarnokd.rxjava2.internal.operators.nbp;

import org.junit.Test;

import hu.akarnokd.rxjava2.NbpObservable;
import hu.akarnokd.rxjava2.functions.*;
import hu.akarnokd.rxjava2.subscribers.nbp.NbpTestSubscriber;

public class NbpOperatorMapNotificationTest {
    @Test
    public void testJust() {
        NbpTestSubscriber<Object> ts = new NbpTestSubscriber<Object>();
        NbpObservable.just(1)
        .flatMap(
                new Function<Integer, NbpObservable<Object>>() {
                    @Override
                    public NbpObservable<Object> apply(Integer item) {
                        return NbpObservable.just((Object)(item + 1));
                    }
                },
                new Function<Throwable, NbpObservable<Object>>() {
                    @Override
                    public NbpObservable<Object> apply(Throwable e) {
                        return NbpObservable.error(e);
                    }
                },
                new Supplier<NbpObservable<Object>>() {
                    @Override
                    public NbpObservable<Object> get() {
                        return NbpObservable.never();
                    }
                }
        ).subscribe(ts);
        
        ts.assertNoErrors();
        ts.assertNotComplete();
        ts.assertValue(2);
    }
}