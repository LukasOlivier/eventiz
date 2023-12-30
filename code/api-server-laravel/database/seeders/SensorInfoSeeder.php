<?php

namespace Database\Seeders;

use App\Models\Category;
use App\Models\Item;
use App\Models\Sensor;
use Illuminate\Database\Seeder;

class SensorInfoSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run()
    {
        $data = CvsReader::read('sensor');

        $model = new Sensor();

        foreach ($data as $row){
            $model->create($row);
        }
    }
}
