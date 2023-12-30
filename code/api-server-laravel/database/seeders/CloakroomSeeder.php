<?php

namespace Database\Seeders;

use App\Models\Category;
use App\Models\Cloakroom;
use App\Models\Item;
use Illuminate\Database\Seeder;

class CloakroomSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run()
    {
        $data = CvsReader::read('cloakrooms');

        $model = new Cloakroom();

        foreach ($data as $row){
            $model->create($row);
        }
    }
}
