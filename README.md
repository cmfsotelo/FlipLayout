# FlipLayout
[![](https://jitpack.io/v/cmfsotelo/fliplayout.svg)](https://jitpack.io/#cmfsotelo/fliplayout)
![Minimum SDK](https://img.shields.io/badge/minSdkVersion%20-15-blue.svg)

Use the FlipLayout as a ViewGroup to easily swap between child views.

<h2>Include in your project</h2>
<h4> In your root/build.gradle</h4>

```groovy
allprojects {
	repositories {
	...
	maven { url 'https://jitpack.io' }
	}
}
```
<h4> In your app/build.gradle</h4>

```groovy
dependencies {
	compile 'com.github.cmfsotelo:fliplayout:v1.0.0'
}
```
<h2>Usage</h2>
Simply use the FrameLayout public methods change which view is visible.

```java
public void showNextChild();
public void showPreviousChild();
```

<h2>Examples (click image to expand code)</h2>
<details>
  <summary><h4>Use to simply flip one view with another</h4><img src="images/JxXiRf.gif"></summary>

```xml
<com.csot.fliplayout.lib.FlipLayout
android:id="@+id/fliplayout2"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:transition="FLIP_X">

	<TextView
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:layout_margin="10dp"
	    android:background="@drawable/circle"
	    android:backgroundTint="#4286f4"
	    android:gravity="center"
	    android:padding="20dp"
	    android:text="G"
	    android:textColor="#fff"
	    android:textSize="40dp"/>

	<ImageView
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:layout_margin="10dp"
	    android:background="@drawable/circle"
	    android:backgroundTint="#555"
	    android:gravity="center"
	    android:padding="20dp"
	    android:src="@drawable/ic_done_black_48dp"
	    android:textStyle="bold"
	    android:tint="#fff"/>
</com.csot.fliplayout.lib.FlipLayout>
```
</details>

<h2>OR</h2>

<details>
  <summary><h4>Use to swap over several views</h4><img src="images/OgR5nT.gif"><img src="images/RSxNeM.gif"></summary>

```xml
<com.csot.fliplayout.lib.FlipLayout
android:id="@+id/fliplayout4"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:transition="TRANSLATE_Y">

	<TextView
	    android:layout_width="wrap_content"
	    android:layout_height="match_parent"
	    android:layout_gravity="center"
	    android:layout_margin="10dp"
	    android:background="#4286f4"
	    android:gravity="center"
	    android:padding="20dp"
	    android:text="FirstView"
	    android:textColor="#fff"
	    android:textSize="20dp"/>

	<TextView
	    android:layout_width="wrap_content"
	    android:layout_height="match_parent"
	    android:layout_gravity="center"
	    android:layout_margin="10dp"
	    android:background="#6016f4"
	    android:gravity="center"
	    android:padding="20dp"
	    android:text="SecondView"
	    android:textColor="#fff"
	    android:textSize="20dp"/>

	<TextView
	    android:layout_width="wrap_content"
	    android:layout_height="match_parent"
	    android:layout_gravity="center"
	    android:layout_margin="10dp"
	    android:background="#f18614"
	    android:gravity="center"
	    android:padding="50dp"
	    android:text="ThirdView"
	    android:textColor="#fff"
	    android:textSize="50dp"/>
</com.csot.fliplayout.lib.FlipLayout>
```

</details>

***

<h2>Customization</h2>

| Attribute |Type           | Description | Default |
|---|---|---|---|
| startingChild | int | First visible child| 0 |
| transitionDuration | int | The ending angle for button disposition   | 200 |
| transition | enum | The type of the transition, one of: FADE, FLIP_X, FLIP_Y, TRANSLATE_X, TRANSLATE_Y, FLIP_Z1, FLIP_Z2 | FLIP_Y |


