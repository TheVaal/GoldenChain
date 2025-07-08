package com.integragames.goldenchain.presentation

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.integragames.goldenchain.BuildConfig
import com.integragames.goldenchain.R
import com.integragames.goldenchain.presentation.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate

@RootNavGraph
@Destination
@Composable
fun GDPRScreen(
    context: Context = LocalContext.current,
    navigator: NavController
) {
    BackHandler {}
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.mipmap.bg),
                contentScale = ContentScale.FillBounds
            )
            .padding(16.dp)
    ) {
        item {
            BaseCard(
                text = "Thank you for using \"Golden Chain\"! This Privacy Notice outlines how we co" +
                        "llect, use, and protect your data. We are committed to ensuring the privacy" +
                        " and security of your information.",
                "Privacy Notice"
            )
        }
        item {
            BaseCard(
                text = "We collect and process the following types of information:\n" +
                        "Google Ad ID (GAID): We collect your Google Advertising ID to personalize " +
                        "advertisements and analyze user behavior.\n" +
                        "Appsflyer Data: Our app uses AppsFlyer services to track and analyze app" +
                        " installations, in-app events, and user engagement. This includes data such " +
                        "as installation attribution, in-app events, and device information.\n",

                "1. Information We Collect"
            )
        }
        item {
            BaseCard(
                text =
                "Install Referrer Data: We collect information about the source of app " +
                        "installations to understand the effectiveness of our marketing efforts.\n" +
                        "Crashlytics Information: To improve app stability, we collect crash reports" +
                        " and related diagnostic information through Crashlytics.",
                ""
            )
        }
        item {
            BaseCard(
                text = "We use the collected information for the following purposes:\n" +
                        "Personalized Advertising: To deliver targeted advertisements based on your interests.\n",
                "2. How We Use Your Information"
            )
        }
        item {
            BaseCard(
                text =
                "App Analytics: To analyze app usage, track installations, and optimize the user experience.\n" +
                        "Install Referrer Analysis: To assess the effectiveness of our marketing channels.\n" +
                        "App Stability Improvement: To identify and fix issues that may cause app crashes.",
                ""
            )
        }
        item {
            BaseCard(
                text = "We want to make it clear that we DO NOT store the collected data on our " +
                        "servers. All data is processed and used within the confines of the app " +
                        "itself and is not retained beyond what is necessary for the mentioned purposes.",
                "3. Data Storage"
            )
        }
        item {

            BaseCard(
                text = "We may share your information with third parties in the following circumstances:\n" +

                        "With Your Explicit Consent: We may share data with third parties if you explicitly " +
                        "consent to such sharing.\n" +

                        "Service Providers: We engage trusted service providers, including AppsFlyer" +
                        " and Crashlytics, to enhance our app's functionality and performance.",
                "4. Data Sharing"
            )
        }
        item {

            BaseCard(
                text = "You have the right to:\n" +

                        "Opt-out of Personalized Advertising: Adjust your advertising preferences on your device.\n" +

                        "Access and Correct Information: Request access to your personal data and correct any inaccuracies.\n" +

                        "Withdraw Consent: If you previously provided consent, you can withdraw it at any time.\n" +

                        "To exercise these rights or for any inquiries about your data, please contact us at everettlabadie2012@coalimail.com.",
                "5. Your Choices and Rights"
            )
        }
        item {

            BaseCard(
                text = "We implement reasonable security measures to protect your information from unauthorized access or disclosure.",
                "6. Data Security"
            )
        }
        item {

            BaseCard(
                text = "We may update this Privacy Notice to reflect changes in our practices. " +
                        "We will notify you of any significant changes through the app.",
                "7. Changes to this Privacy Notice"
            )
        }
        item {

            BaseCard(
                text = "By using \"Golden Chain,\" you consent to the collection and processing of your " +
                        "data as described in this Privacy Notice.\n" +
                        "Thank you for using \"Golden Chain.\" If you have any concerns or questions," +
                        " please don't hesitate to contact us.",
                "8. Consent"
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    onClick = {
                        context.getSharedPreferences(
                            BuildConfig.APPLICATION_ID,
                            Context.MODE_PRIVATE
                        ).edit().putBoolean("isFirstTime", true).apply()
                        navigator.navigate(HomeScreenDestination)
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.disagree),
                        contentDescription = "Disagree button"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp),
                    onClick = {
                        context.getSharedPreferences(
                            BuildConfig.APPLICATION_ID,
                            Context.MODE_PRIVATE
                        ).edit().putBoolean("isFirstTime", true).apply()
                        navigator.navigate(HomeScreenDestination)
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.agree),
                        contentDescription = "Agree button"
                    )
                }
            }
        }

    }
}

@Composable
fun BaseCard(text: String, title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .paint(
                painterResource(id = R.mipmap.frame2),
                contentScale = ContentScale.FillBounds
            )
            .padding(top = 36.dp, bottom = 48.dp, start = 52.dp, end = 36.dp),
        color = Color.White,
        text = buildAnnotatedString {
            if (title.isNotEmpty()) {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("\n$title")
                    }
                }
            }
            withStyle(style = ParagraphStyle(textAlign = TextAlign.Left)) {
                append(
                    text
                )
            }
        },
    )
    Spacer(modifier = Modifier.height(16.dp))
}