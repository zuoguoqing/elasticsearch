/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.rankeval;

import org.elasticsearch.action.support.ToXContentToBytes;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.test.ESTestCase;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RankEvalTestHelper {

    public static XContentParser roundtrip(ToXContentToBytes testItem) throws IOException {
        XContentBuilder builder = XContentFactory.contentBuilder(ESTestCase.randomFrom(XContentType.values()));
        if (ESTestCase.randomBoolean()) {
            builder.prettyPrint();
        }
        testItem.toXContent(builder, ToXContent.EMPTY_PARAMS);
        XContentBuilder shuffled = ESTestCase.shuffleXContent(builder);
        XContentParser itemParser = XContentHelper.createParser(shuffled.bytes());
        return itemParser;
    }

    public static void testHashCodeAndEquals(Object testItem, Object mutation, Object secondCopy) {
        assertFalse("testItem is equal to null", testItem.equals(null));
        assertFalse("testItem is equal to incompatible type", testItem.equals(""));
        assertTrue("testItem is not equal to self", testItem.equals(testItem));
        assertThat("same testItem's hashcode returns different values if called multiple times", testItem.hashCode(),
                equalTo(testItem.hashCode()));

        assertThat("different testItem should not be equal", mutation, not(equalTo(testItem)));

        assertNotSame("testItem copy is not same as original", testItem, secondCopy);
        assertTrue("testItem is not equal to its copy", testItem.equals(secondCopy));
        assertTrue("equals is not symmetric", secondCopy.equals(testItem));
        assertThat("testItem copy's hashcode is different from original hashcode", secondCopy.hashCode(),
                equalTo(testItem.hashCode()));
    }
}