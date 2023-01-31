/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getstream.chat.docs.java.ui.guides.realm.entities

import io.getstream.chat.android.models.SyncStatus

public fun Int.toDomain(): SyncStatus =
    when (this) {
        SyncStatus.SYNC_NEEDED.status -> SyncStatus.SYNC_NEEDED
        SyncStatus.COMPLETED.status -> SyncStatus.COMPLETED
        SyncStatus.FAILED_PERMANENTLY.status -> SyncStatus.FAILED_PERMANENTLY
        SyncStatus.IN_PROGRESS.status -> SyncStatus.IN_PROGRESS
        SyncStatus.AWAITING_ATTACHMENTS.status -> SyncStatus.AWAITING_ATTACHMENTS
        else -> throw IllegalStateException("The status code: $this is not supported")
    }

public fun SyncStatus.toRealm(): Int = status
