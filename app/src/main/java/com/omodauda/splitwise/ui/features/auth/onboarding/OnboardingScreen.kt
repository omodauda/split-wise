package com.omodauda.splitwise.ui.features.auth.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.onboardingData
import com.omodauda.splitwise.ui.theme.ButtonLarge
import com.omodauda.splitwise.ui.theme.OnboardingTitle
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.ui.theme.inter
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    goToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val data = onboardingData
    val pagerState = rememberPagerState(pageCount = {
        data.size
    })
    val coroutineScope = rememberCoroutineScope()
    val goToNextPage = {
        if (pagerState.currentPage < data.size - 1) {
            coroutineScope.launch {
                pagerState.scrollToPage(pagerState.settledPage + 1)
            }
        } else {
            goToLogin()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = innerPadding.calculateBottomPadding(), start = innerPadding.calculateStartPadding(
                    LayoutDirection.Ltr), end = innerPadding.calculateEndPadding(LayoutDirection.Ltr))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
            ) { page ->
                val currentPageData = data[page]
                val titles1 = stringArrayResource(currentPageData.titles)
                val titles2 = stringArrayResource(currentPageData.title2)
                val descriptions = stringArrayResource(currentPageData.description)

                PagerView(
                    image = currentPageData.image,
                    title1 = titles1[page],
                    title2 = titles2[page],
                    description = descriptions[page],
                    paddingTop = innerPadding.calculateTopPadding(),
                    isLandscape = isLandscape
                )
            }
            Spacer(Modifier.height( if (isLandscape) 16.dp else 80.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ScreenDimensions.largePadding)
                    .padding(bottom = 16.dp )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val isCurrent = pagerState.currentPage == iteration
                        val width = if (isCurrent) 32.dp else 8.dp
                        val color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        Box(
                            modifier = Modifier
                                .width(width)
                                .height(8.dp)
                                .background(color = color, shape = SplitWiseShapes.appIcon)

                        )
                    }
                }

                Button(
                    onClick = { goToNextPage() },
                    shape = SplitWiseShapes.button,
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = ScreenDimensions.verticalPadding)
                ) {
                    val isLast = pagerState.currentPage == data.size - 1
                    val text = if (!isLast) stringResource(R.string.next) else stringResource(R.string.get_started)
                    Text(
                        text = text,
                        style = ButtonLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(Modifier.width(Spacing.medium))
                    Icon(
                        painter = painterResource(if (isLast) R.drawable.check_icon else R.drawable.forward_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

    }
}

@Composable
fun PagerView(
    image: Int,
    title1: String,
    title2: String,
    description: String,
    paddingTop: Dp,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnboardingImgView(
                image = image,
                paddingTop = paddingTop,
                modifier = Modifier.weight(1f)
            )

            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(horizontal = ScreenDimensions.largePadding)
                    .verticalScroll(rememberScrollState())

            ) {
                Text(text = title1, style = OnboardingTitle, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(Spacing.small))
                Text(text = title2, style = OnboardingTitle, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(Spacing.small))
                Text(
                    text = description,
                    style = TextStyle(fontFamily = inter, fontWeight = FontWeight.Normal, fontSize = 18.sp, lineHeight = 29.3.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            OnboardingImgView(
                image = image,
                paddingTop = paddingTop,
                modifier = Modifier.weight(0.5f)
            )

            Column(
                modifier = Modifier
                    .padding(start = ScreenDimensions.largePadding, end = ScreenDimensions.largePadding, top = 43.dp)
            ) {
                Text(
                    text = title1,
                    style = OnboardingTitle,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(Spacing.small))
                Text(
                    text = title2,
                    style = OnboardingTitle,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(Spacing.small))
                Text(
                    text = description,
                    style = TextStyle(fontFamily = inter, fontWeight = FontWeight.Normal, fontSize = 18.sp, lineHeight = 29.3.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun OnboardingImgView(
    image: Int,
    paddingTop: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = paddingTop + ScreenDimensions.sectionSpacing, start = ScreenDimensions.sectionSpacing, end = ScreenDimensions.sectionSpacing, bottom = ScreenDimensions.sectionSpacing)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .clip(SplitWiseShapes.onboardingImage)
        )
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun OnboardingScreenPreview() {
    SplitWiseTheme {
        OnboardingScreen(goToLogin = {})
    }
}