<?php

namespace App\Http\Controllers;

use App\Modules\Services\RackService;
use Illuminate\Http\Request;

class RackController extends Controller
{
    private RackService $service;

    public function __construct(RackService $service)
    {
        $this->service = $service;
    }

    public function get(Request $request)
    {
        $occupied = $request->query('occupied');
        return $this->service->get($occupied);
    }

    public function free(int $rack)
    {
        return $this->service->free($rack);
    }

    public function occupy(Request $request)
    {
        $value = $request->all();
        return $this->service->occupy($value);
    }
}
