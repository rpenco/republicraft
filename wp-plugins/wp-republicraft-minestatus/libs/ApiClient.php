<?php

class ApiClient
{
    private $host;
    private $port;

    /**
     * @param $host
     * @param int $port
     */
    public function __construct($host, $port = 25565)
    {
        // Setup host and port of minecraft server
        $this->host = $host;
        $this->port = $port;
    }

    /**
     * @return mixed
     */
    public function call()
    {
        $ms = new MineStat($this->host, $this->port);
        if($ms->is_online())
        {
            return $ms;
        }
        else
        {
            return null;
        }

    }
}
