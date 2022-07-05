package com.manoj.github.modularadapter.itemmanager.sortedlist;

import com.manoj.github.modularadapter.itemmanager.ChangeSet;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 01/04/2017
 */
interface ChangeConsumer extends ChangeSet.MoveCallback, ChangeSet.AddCallback, ChangeSet.RemoveCallback, ChangeSet.ChangeCallback {

}
