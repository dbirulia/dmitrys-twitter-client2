// Generated code from Butter Knife. Do not modify!
package com.birulia.apps.dmitrytwitterclient;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.apps.dmitrytwitterclient.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ProfileActivity_ViewBinding<T extends ProfileActivity> implements Unbinder {
  protected T target;

  public ProfileActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.tvProfileName = finder.findRequiredViewAsType(source, R.id.tvProfileName, "field 'tvProfileName'", TextView.class);
    target.tvUserName = finder.findRequiredViewAsType(source, R.id.tvUserName, "field 'tvUserName'", TextView.class);
    target.ivProfileBackground = finder.findRequiredViewAsType(source, R.id.ivProfileBackground, "field 'ivProfileBackground'", ImageView.class);
    target.ivProfileAvatar = finder.findRequiredViewAsType(source, R.id.ivProfileAvatar, "field 'ivProfileAvatar'", ImageView.class);
    target.tvLocation = finder.findRequiredViewAsType(source, R.id.tvLocation, "field 'tvLocation'", TextView.class);
    target.tvFollowersCount = finder.findRequiredViewAsType(source, R.id.tvFollowersCount, "field 'tvFollowersCount'", TextView.class);
    target.tvFollowingCount = finder.findRequiredViewAsType(source, R.id.tvFollowingCount, "field 'tvFollowingCount'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvProfileName = null;
    target.tvUserName = null;
    target.ivProfileBackground = null;
    target.ivProfileAvatar = null;
    target.tvLocation = null;
    target.tvFollowersCount = null;
    target.tvFollowingCount = null;

    this.target = null;
  }
}
