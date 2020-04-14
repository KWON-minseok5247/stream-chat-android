package io.getstream.chat.android.livedata

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.getstream.chat.android.client.api.models.ChannelWatchRequest
import io.getstream.chat.android.client.api.models.Pagination
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.utils.Result
import io.getstream.chat.android.client.utils.SyncStatus
import io.getstream.chat.android.livedata.utils.ChatCallTestImpl
import io.getstream.chat.android.livedata.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChannelRepoReadPaginateTest: BaseTest() {

    @Before
    fun setup() {
        client = createClient()
        setupRepo(client, true)
    }

    @After
    fun tearDown() {
        db.close()
        client.disconnect()
    }

    @Test
    @Ignore
    fun loadNewerMessages() = runBlocking(Dispatchers.IO) {
        val channelRepo = repo.channel("messaging", "testabc")
        Truth.assertThat(channelRepo.loading.getOrAwaitValue()).isFalse()
        channelRepo.upsertMessages(listOf(data.message1, data.message2Older))
        // verify we sort correctly
        val messages = channelRepo.sortedMessages()
        Truth.assertThat(messages[0].createdAt!!.before(messages[1].createdAt)).isTrue()
        // verify we generate the right request
        val request = channelRepo.loadMoreMessagesRequest(10, Pagination.GREATER_THAN)
        val filter = request.messages.get("id_gt") ?: ""
        // message 2 is older, we should use message1 for filtering on newer messages
        Truth.assertThat(filter).isEqualTo(data.message1.id)
        // verify that running the query doesn't error

        val result = Result(data.channel1, null)

        val channelMock = client.channel(data.channel1.type, data.channel1.id)

        val args = any<ChannelWatchRequest>()
        whenever(channelMock.watch(args)).thenReturn(ChatCallTestImpl(result))

        channelRepo.runChannelQueryOnline(request)
    }

    /**
     * test that a message added only to the local storage is picked up
     */
    @Test
    fun watchSetsMessagesAndChannelOffline() = runBlocking(Dispatchers.IO) {
        repo.setOffline()
        // add a message to local storage
        repo.repos.users.insert(data.user1)
        repo.repos.channels.insert(data.channel1)
        channelRepo._sendMessage(data.message1)
        // remove the livedata
        channelRepo = ChannelRepo(data.channel1.type, data.channel1.id, repo.client, repo)

        // run watch while we're offline
        channelRepo._watch()

        // the message should still show up
        val messages = channelRepo.messages.getOrAwaitValue()
        val channel = channelRepo.channel.getOrAwaitValue()

        Truth.assertThat(messages).isNotEmpty()
        Truth.assertThat(channel).isNotNull()
        Truth.assertThat(channel.config).isNotNull()
    }

    /**
     * test that a message added only to the local storage is picked up
     */
    @Test
    fun watchSetsMessagesAndChannelOnline() = runBlocking(Dispatchers.IO) {
        repo.setOnline()
        // setup an online message
        val message = Message()
        message.syncStatus = SyncStatus.SYNC_NEEDED
        // write a message
        channelRepo._sendMessage(message)

        val messages = channelRepo.messages.getOrAwaitValue()
        val channel = channelRepo.channel.getOrAwaitValue()

        Truth.assertThat(messages.size).isGreaterThan(0)
        Truth.assertThat(messages.first().id).isEqualTo(message.id)
        Truth.assertThat(channel).isNotNull()
        Truth.assertThat(channel.config).isNotNull()
    }

    @Test
    fun recovery() = runBlocking(Dispatchers.IO) {
        // running recover should trigger channels to show up for active queries and channels
        repo.connectionRecovered(true)

        // verify channel data is loaded
        val channelRepos = queryRepo.channels.getOrAwaitValue()
        Truth.assertThat(channelRepos.size).isGreaterThan(0)

        // verify we have messages as well
        val channelId = channelRepos.first().cid

        val messages = repo.channel(channelId).messages.getOrAwaitValue()
        Truth.assertThat(messages.size).isGreaterThan(0)
    }



    @Test
    fun loadOlderMessages() {
        val channelRepo = repo.channel("messaging", "testabc")
        Truth.assertThat(channelRepo.loading.getOrAwaitValue()).isFalse()
        channelRepo.upsertMessages(listOf(data.message1, data.message2Older))
        // verify we sort correctly
        val messages = channelRepo.sortedMessages()
        Truth.assertThat(messages[0].createdAt!!.before(messages[1].createdAt)).isTrue()
        // verify we generate the right request
        val request = channelRepo.loadMoreMessagesRequest(10, Pagination.LESS_THAN)
        // message 2 is older, we should use message 2 for getting older messages
        Truth.assertThat(request.messageFilterValue).isEqualTo(data.message2Older.id)
        // verify that running the query doesn't error
        runBlocking(Dispatchers.IO) {channelRepo.runChannelQueryOnline(request)}
        // TODO: Mock the call to query channel
    }


}