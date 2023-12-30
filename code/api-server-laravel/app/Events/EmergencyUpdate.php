<?php

namespace App\Events;

use App\Models\Cloakroom;
use App\Models\Emergency;
use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class EmergencyUpdate implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    /**
     * Create a new event instance.
     */

    protected Emergency $model;

    public function __construct(Emergency $model)
    {
        $this->model = $model;
    }


    public function broadcastWith(): array
    {
        return [
            'type' => $this->model->type,
            'value' => $this->model->location,
            'created_at' => $this->model->created_at,
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
            new Channel('emergencies'),
        ];
    }
}
