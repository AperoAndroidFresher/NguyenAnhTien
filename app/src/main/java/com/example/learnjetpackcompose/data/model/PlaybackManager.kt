package com.example.learnjetpackcompose.data.model

import com.example.learnjetpackcompose.RoomDB.Entity.Song
import com.example.learnjetpackcompose.Screen.Player.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random
object PlaybackManager {

    enum class QueueSource {
        LOCAL,
        REMOTE,
        PLAYLIST
    }

    private val _currentSong = MutableStateFlow<Song?>(null)
    private val _isPlaying = MutableStateFlow(false)

    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // Queue state
    private val _queueSongs = MutableStateFlow<List<Song>>(emptyList())
    private val _currentIndex = MutableStateFlow(-1)
    private val _queueSource = MutableStateFlow(QueueSource.LOCAL)
    private val _queueId = MutableStateFlow<String?>(null)

    // Modes
    private val _isShuffle = MutableStateFlow(false)
    private val _repeatMode = MutableStateFlow(RepeatMode.NONE)

    val queueSongs: StateFlow<List<Song>> = _queueSongs.asStateFlow()
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    val queueSource: StateFlow<QueueSource> = _queueSource.asStateFlow()
    val queueId: StateFlow<String?> = _queueId.asStateFlow()

    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val playedSet: MutableSet<Int> = LinkedHashSet()
    private val historyStack: ArrayDeque<Int> = ArrayDeque()

    fun setNowPlaying(song: Song?) {
        _currentSong.value = song
    }

    fun setIsPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }

    fun setQueue(
        songs: List<Song>,
        startIndex: Int,
        source: QueueSource,
        id: String? = null
    ) {
        _queueSongs.value = songs
        _queueSource.value = source
        _queueId.value = id

        playedSet.clear()
        historyStack.clear()

        if (songs.isEmpty()) {
            _currentIndex.value = -1
            setNowPlaying(null)
            setIsPlaying(false)
            return
        }

        val boundedIndex = startIndex.coerceIn(0, songs.lastIndex)
        playAt(boundedIndex)
        playedSet.add(boundedIndex)
    }

    fun playAt(index: Int) {
        if (_queueSongs.value.isEmpty()) return
        val boundedIndex = index.coerceIn(0, _queueSongs.value.lastIndex)
        _currentIndex.value = boundedIndex
        setNowPlaying(_queueSongs.value[boundedIndex])
    }

    fun toggleShuffle() {
        val newValue = !_isShuffle.value
        _isShuffle.value = newValue
        playedSet.clear()
        historyStack.clear()
        val idx = _currentIndex.value
        if (idx >= 0) playedSet.add(idx)
    }

    fun cycleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.NONE -> RepeatMode.REPEAT_ALL
            RepeatMode.REPEAT_ALL -> RepeatMode.REPEAT_ONE
            RepeatMode.REPEAT_ONE -> RepeatMode.NONE
        }
    }

    fun nextManual(): Song? {
        val songs = _queueSongs.value
        if (songs.isEmpty()) return null
        val current = _currentIndex.value
        if (current < 0) return null

        return if (_isShuffle.value) {

            historyStack.addLast(current)
            playedSet.add(current)

            val nextIndex = pickNextShuffleIndexOrReset(manualAction = true)
            playAt(nextIndex)
            songs[nextIndex]
        } else {
            val nextIndex = (current + 1) % songs.size
            playAt(nextIndex)
            songs[nextIndex]
        }
    }

    fun previousManual(): Song? {
        val songs = _queueSongs.value
        if (songs.isEmpty()) return null
        val current = _currentIndex.value
        if (current < 0) return null

        return if (_isShuffle.value) {
            if (historyStack.isNotEmpty()) {
                val prevIndex = historyStack.removeLast()
                playAt(prevIndex)
                songs[prevIndex]
            } else {
                val prevIndex = if (current == 0) songs.lastIndex else current - 1
                playAt(prevIndex)
                songs[prevIndex]
            }
        } else {
            val prevIndex = if (current == 0) songs.lastIndex else current - 1
            playAt(prevIndex)
            songs[prevIndex]
        }
    }

    fun onSongCompleted(): Song? {
        val songs = _queueSongs.value
        if (songs.isEmpty()) return null
        val current = _currentIndex.value
        if (current < 0) return null

        return when (_repeatMode.value) {
            RepeatMode.REPEAT_ONE -> {
                playAt(current)
                songs[current]
            }
            RepeatMode.NONE, RepeatMode.REPEAT_ALL -> {
                if (_isShuffle.value) {
                    playedSet.add(current)
                    val exhausted = playedSet.size >= songs.size
                    if (exhausted && _repeatMode.value == RepeatMode.NONE) {
                        null
                    } else {
                        if (exhausted) {
                            playedSet.clear()
                            historyStack.clear()
                        }
                        val nextIndex = pickNextShuffleIndexOrReset(manualAction = false)
                        playAt(nextIndex)
                        songs[nextIndex]
                    }
                } else {

                    val isLast = current >= songs.lastIndex
                    if (isLast && _repeatMode.value == RepeatMode.NONE) {
                        null
                    } else {
                        val nextIndex = if (isLast) 0 else current + 1
                        playAt(nextIndex)
                        songs[nextIndex]
                    }
                }
            }
        }
    }

    private fun pickNextShuffleIndexOrReset(manualAction: Boolean): Int {
        val songs = _queueSongs.value
        val current = _currentIndex.value
        if (songs.isEmpty() || current < 0) return 0

        val candidates = songs.indices.filter { it != current && !playedSet.contains(it) }
        if (candidates.isNotEmpty()) {
            return candidates.random(Random)
        }
        playedSet.clear()
        historyStack.clear()
        val newCandidates = songs.indices.filter { it != current }
        return if (newCandidates.isNotEmpty()) newCandidates.random(Random) else current
    }
}

