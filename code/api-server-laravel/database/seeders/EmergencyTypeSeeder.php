<?php

namespace Database\Seeders;

use App\Models\Category;
use App\Models\EmergencyType;
use App\Models\Item;
use Illuminate\Database\Seeder;

class EmergencyTypeSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run()
    {
        $data = CvsReader::read('emergency_types');

        $model = new EmergencyType();

        foreach ($data as $row){
            $model->create($row);
        }
    }
}
