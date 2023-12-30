<?php

namespace App\Modules\Services;


use App\Events\SensorUpdate;
use App\Models\Sensor;
use App\Models\SensorStatistic;
use Illuminate\Support\Facades\DB;

class SensorStatisticsService
{
    protected SensorStatistic $model;

    public function __construct(SensorStatistic $model)
    {
        $this->model = $model;
    }


    public function getStatistics($type)
    {
        $query = $this->model->query();

        if ($type != null) {
            $query->where('type', $type);
        }

        $all= $query->select([
            'type',
            'value',
            DB::raw('DATE_FORMAT(created_at, "%H:%i:%s") as timestamp'),
        ])->get();

        $all->push([
            'type' => 'average',
            'value' => $all->take(10)->avg('value'),
        ]);
        return $all;
    }
}
