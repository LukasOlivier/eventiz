<?php

namespace App\Modules\Services;


use App\Events\AlarmUpdate;
use App\Events\SensorUpdate;
use App\Models\Sensor;
use App\Models\SensorStatistic;

class SensorService
{
    protected Sensor $model;

    public function __construct(Sensor $model)
    {
        $this->model = $model;
    }

    public function get($type)
    {
        if ($type == null) {
            return $this->model->all();
        }
        return $this->model->where('type', $type)->get();
    }

    public function patch($type, $value)
    {
        $sensor = $this->model->where('type', $type)->first();
        $sensor->value = $value;
        $sensor->save();

        SensorStatistic::create([
            'type' => $type,
            'value' => $value,
        ]);
        if (SensorStatistic::count() > 50) {
            SensorStatistic::orderBy('created_at', 'asc')->first()->delete();
        }


        $average = SensorStatistic::where('type', $type)->take(10)->avg('value');

        if ($average > 30) {
            event(new AlarmUpdate());
        }
        broadcast(new SensorUpdate($sensor));
        return $sensor;
    }
}
