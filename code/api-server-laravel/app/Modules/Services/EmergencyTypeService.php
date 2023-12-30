<?php

namespace App\Modules\Services;


use App\Models\EmergencyType;

class EmergencyTypeService
{
    protected EmergencyType $model;

    public function __construct(EmergencyType $model)
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
}
