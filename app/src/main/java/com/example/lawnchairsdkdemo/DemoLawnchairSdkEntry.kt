package com.example.lawnchairsdkdemo

import android.content.Context
import android.content.Intent
import android.util.Log
import app.lawnchair.sdk.LawnchairOperations
import app.lawnchair.sdk.api.DesktopVisibleListener
import app.lawnchair.sdk.api.DesktopItemClickAction
import app.lawnchair.sdk.api.DesktopItemPlacement
import app.lawnchair.sdk.api.DesktopItemProvider
import app.lawnchair.sdk.api.DesktopItemRowAnchor
import app.lawnchair.sdk.api.DesktopItemSpec
import app.lawnchair.sdk.api.LawnchairSdkEntry
import app.lawnchair.sdk.api.MinusOneContentProvider
import app.lawnchair.sdk.api.MinusOneOverlayStateListener

class DemoLawnchairSdkEntry : LawnchairSdkEntry {
    override val minusOneContentProvider: MinusOneContentProvider = DemoMinusOneContentProvider()
    override val openMinusOneOnLauncherLaunch: Boolean = false
    override val minusOneOverlayStateListener: MinusOneOverlayStateListener =
        MinusOneOverlayStateListener { state, progress ->
            DemoMinusOneOverlayMonitor.update(state, progress)
        }
    override val desktopVisibleListener: DesktopVisibleListener =
        DesktopVisibleListener { source ->
            Log.d(TAG, "Desktop fully visible, source=$source")
        }

    override val desktopItemProvider: DesktopItemProvider = DemoDesktopItemProvider()

    private companion object {
        const val TAG = "DemoLawnchairSdk"
    }
}

private class DemoDesktopItemProvider : DesktopItemProvider {
    override fun provideDesktopItems(context: Context): List<DesktopItemSpec> {
        return listOf(
            DesktopItemSpec(
                title = context.getString(R.string.demo_open_minus_one_label),
                iconResId = R.drawable.ic_demo_open_minus_one,
                clickAction = DesktopItemClickAction.ProviderAction(ACTION_OPEN_MINUS_ONE),
                allowDragToDelete = false,
                placement = DesktopItemPlacement(
                    page = 1,
                    column = 0,
                    row = DesktopItemRowAnchor.FromBottom(1),
                ),
            ),
            DesktopItemSpec(
                title = context.getString(R.string.demo_desktop_entry_1_label),
                iconResId = R.drawable.ic_demo_desktop_entry,
                clickAction = DesktopItemClickAction.LaunchIntent(
                    Intent(context, DemoDesktopEntryActivity::class.java),
                ),
                allowDragToDelete = false,
                placement = DesktopItemPlacement(
                    page = 1,
                    column = 0,
                    row = DesktopItemRowAnchor.FromBottom(0),
                ),
            ),
            DesktopItemSpec(
                title = context.getString(R.string.demo_desktop_entry_2_label),
                iconResId = R.drawable.ic_demo_desktop_entry,
                clickAction = DesktopItemClickAction.LaunchIntent(
                    DemoDesktopEntryActivity.createIntent(
                        context = context,
                        entryTitle = context.getString(R.string.demo_desktop_entry_2_title),
                        entryMessage = context.getString(R.string.demo_desktop_entry_2_message),
                    ),
                ),
                id = DESKTOP_ITEM_ID_ENTRY_2,
                allowDragToDelete = false,
                placement = DesktopItemPlacement(
                    page = 1,
                    column = 1,
                    row = DesktopItemRowAnchor.FromBottom(0),
                ),
            ),
        )
    }

    override fun onDesktopItemAction(
        context: Context,
        itemId: String,
        actionId: String,
    ): Boolean {
        if (actionId == ACTION_OPEN_MINUS_ONE) {
            return LawnchairOperations.openMinusOneAnimated(context)
        }
        return false
    }

    private companion object {
        const val ACTION_OPEN_MINUS_ONE = "open_minus_one"
        const val DESKTOP_ITEM_ID_ENTRY_2 = "demo_entry_2"
    }
}
