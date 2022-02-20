package ru.aasmc.wordify.common.uicomponents.extensions

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeDismissSnackBar(
    data: SnackbarData,
    onDismiss: (() -> Unit)? = null,
    snackBar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    val dismissState = rememberDismissState {
        if (it != DismissValue.Default) {
            // initially dismiss the snack bar
            data.dismiss()
            // invoke the callback
            onDismiss?.invoke()
        }
        true
    }
    SwipeToDismiss(
        state = dismissState,
        background = {},
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        dismissContent = { snackBar(data) }
    )
}

@Composable
fun SwipeDismissSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { hostState.currentSnackbarData?.dismiss() },
    snackBar: @Composable (SnackbarData) -> Unit = { data ->
        SwipeDismissSnackBar(data = data, onDismiss = onDismiss)
    }
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = snackBar,
        modifier = modifier
    )
}

















