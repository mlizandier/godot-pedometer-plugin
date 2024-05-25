package mlizandier.godot.pedometer.plugin

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import android.widget.Toast
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class GodotAndroidPlugin(godot: Godot): GodotPlugin(godot) {
    private var numberOfSteps = 0;
    private val stepCounterListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val steps = event?.values?.get(0)?.toInt() ?: 0
            updateSteps(steps)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes here
        }
    }
    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME;

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf()
    }

    override fun onMainCreate(activity: Activity?): View? {
        val sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        sensorManager.registerListener(stepCounterListener, stepCounter, SensorManager.SENSOR_DELAY_NORMAL)
        return super.onMainCreate(activity);
    }

    /**
     * Example showing how to declare a method that's used by Godot.
     *
     * Shows a 'Hello World' toast.
     */
    @UsedByGodot
    private fun helloWorld() {
        runOnUiThread {
            Toast.makeText(activity, "Hello World", Toast.LENGTH_LONG).show()
            Log.v(pluginName, "Hello World")
        }
    }
    private fun updateSteps(steps: Int) {
        // Store the steps in a variable or send them to Godot here
        numberOfSteps = steps;
    }

    @UsedByGodot
    private fun getSteps(): Int {
        return numberOfSteps;
    }
}
