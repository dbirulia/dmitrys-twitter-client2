// Generated code from Butter Knife. Do not modify!
package com.birulia.apps.dmitrytwitterclient;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.apps.dmitrytwitterclient.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MessagesActivity_ViewBinding<T extends MessagesActivity> implements Unbinder {
  protected T target;

  public MessagesActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.rvMessages = finder.findRequiredViewAsType(source, R.id.rvMessages, "field 'rvMessages'", RecyclerView.class);
    target.actionBar = finder.findRequiredViewAsType(source, R.id.actionBar, "field 'actionBar'", Toolbar.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rvMessages = null;
    target.actionBar = null;

    this.target = null;
  }
}
