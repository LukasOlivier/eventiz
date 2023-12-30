<?php

namespace App\Events;

use App\Models\Sensor;
use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class SensorUpdate implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    /**
     * Create a new event instance.
     */
    protected Sensor $model;

    public function __construct(Sensor $model)
    {
        $this->model = $model;
    }


    public function broadcastWith(): array
    {
        return [
            'type' => $this->model->type,
            'value' => $this->model->value,
            'timestamp' => $this->model->created_at->format('H:i:s'),
        ];
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return array<int, Channel>
     */
    public function broadcastOn(): array
    {
        return [
            new Channel('sensors'),
        ];
    }
}
