<?php

namespace Database\Seeders;

use App\Models\Category;
use App\Models\Item;
use App\Models\Rack;
use Illuminate\Database\Seeder;

class RackSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run()
    {
        $data = CvsReader::read('racks');

        $model = new Rack();

        foreach ($data as $row){
            // if value is empty set null in database
            foreach ($row as $key => $value) {
                if ($value == '') {
                    $row[$key] = null;
                }
            }
            $model->create($row);
        }
    }
}
