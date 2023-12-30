<?php

namespace App\Http\Controllers;

use App\Modules\Services\SensorService;
use App\Modules\Services\SensorStatisticsService;
use Illuminate\Http\Request;

class SensorStatisticsController extends Controller
{
    private SensorStatisticsService $service;

    public function __construct(SensorStatisticsService $service)
    {
        $this->service = $service;
    }

    public function getStatistics(Request $request)
    {
        $type = $request->query('type');
        $filter = $request->query('filter');
        return $this->service->getStatistics($type, $filter);
    }

}
