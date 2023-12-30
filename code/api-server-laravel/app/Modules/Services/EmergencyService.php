<?php

namespace App\Modules\Services;


use App\Events\EmergencyUpdate;
use App\Models\Emergency;
use App\Models\EmergencyType;

class EmergencyService
{
    protected Emergency $model;

    public function __construct(Emergency $model)
    {
        $this->model = $model;
    }

    public function get($type)
    {
        if ($type == null) {
            return $this->model->all();
        }
        $type_id = EmergencyType::where('type', $type)->first()->id;
        return $this->model->where('type_id', $type_id)->get();
    }

    public function post($emergency)
    {
        $type = EmergencyType::where('type', $emergency['type'])->first();
        $emergency =  $this->model->create([
            'type_id' => $type->id,
            'location' => $emergency['location'],
        ]);
        broadcast(new EmergencyUpdate($emergency));
        return $emergency;
    }

}
