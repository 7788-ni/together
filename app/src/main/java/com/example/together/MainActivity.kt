package com.example.together

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.together.notify.ChatNotifier
import com.example.together.ui.LocalAppContainer
import com.example.together.ui.TogetherNavHost
import com.example.together.ui.theme.ThemeBackdrop
import com.example.together.ui.theme.ThemeTokenCodec
import com.example.together.ui.theme.TogetherTheme

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = AppContainer(applicationContext)
        consumeOpenChatIntent(intent)
        setContent {
            // 主题方案全局订阅：列表里切换/编辑保存后立即生效，无需重启
            val themeProfile by appContainer.themeRepository.observeSelected()
                .collectAsState(initial = null)
            // 解码失败 → null → TogetherTheme 回退内置默认，主题问题不阻塞启动
            val tokens = themeProfile?.let { ThemeTokenCodec.decode(it.tokensJson) }
            CompositionLocalProvider(LocalAppContainer provides appContainer) {
                TogetherTheme(tokens) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ThemeBackdrop()
                        TogetherNavHost()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        ChatNotifier.appInForeground = true
    }

    override fun onStop() {
        super.onStop()
        ChatNotifier.appInForeground = false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        consumeOpenChatIntent(intent)
    }

    /** 聊天通知点击 → 置标志，导航层据此切到聊天页。 */
    private fun consumeOpenChatIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(ChatNotifier.EXTRA_OPEN_CHAT, false) == true) {
            ChatNotifier.pendingOpenChat = true
        }
    }
}
