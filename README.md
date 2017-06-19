# Godot-ShareImage
This is an Android module for [Godot Engine](https://github.com/okamstudio/godot).

This module can create images in Android External storage and share them via Intents.

## Installation
- Copy `shareimage` folder inside the `modules` directory of the Godot source.

- You must [recompile](https://godot.readthedocs.io/en/stable/development/compiling/compiling_for_android.html) Godot for Android.

- You need to grant both `WRITE_EXTERNAL_STORAGE` and `READ_EXTERNAL_STORAGE` permissions in order for this module to work properly. **Important:** Starting with Android 6.0 (API level 23) it's not enough to just include the permissions in your `export.cfg`. You must request them from the user at runtime. To do so, you can use this [module](https://github.com/vanyasem/Godot-AndroidPermissions).

- Finally, you need to include the module in your `engine.cfg` (if you have more than one module separate them by comma):
```
[android]
modules="org/godotengine/godot/ShareImage"
```

## How to use
It's very simple, here's a small snipper:

```python
var share_image = null
func _ready():
	if Globals.has_singleton("ShareImage"):
		share_image = Globals.get_singleton("ShareImage")
		share_image.init(get_instance_ID(), "Share using:", false)
		screen_capture()
	pass

var img = null
func screen_capture():
	get_viewport().queue_screen_capture()
	yield(get_tree(), "idle_frame")
	yield(get_tree(), "idle_frame")
	img = get_viewport().get_screen_capture()

	share_image.getAndroidExternalPath("ExampleFileName_" + str(OS.get_unix_time()) + ".png")
	pass

func _on_path_returned(path):
	if path != "":
		img.save_png(path)
		share_image.shareImageWithText(path, "Example image sending using ShareImage module")
	pass
```

## API Reference
The following methods are available:

```python
# Init ShareImage
# @param int instanceId The instance id from Godot (get_instance_ID())
# @param String shareTitle A title for an intent chooser
# @param boolean debug Indicates whether a debug mode should be enabled
init(instanceId, shareTitle, debug)

# Get external path for a file with given name
# @param String fileName The name of a file
getAndroidExternalPath(fileName)

# Path returned callback
# @param String path Path of an image
_on_path_returned(path)

# Share image
# @param String path Path to image file without file://
shareImage(path)

# Share image with text
# @param String path Path to image file without file://
# @param String shareText Text to share
shareImageWithText(path, shareText)
```
