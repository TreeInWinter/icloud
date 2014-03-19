package com.travelzen.framework.thrift.standardserver;
 

public class Config
{
	
	
	
	//beginAddByRobin
	public String zookeeper_connect_string;
	public Integer total_memusage_in_mb;
	//endAddByRobin
	
	
    public String cluster_name = "Test Cluster";
    public String authenticator;
    public String authority;
    
    /* Hashing strategy Random or OPHF */
    public String partitioner;
    
    public Boolean auto_bootstrap = false;
    public Boolean hinted_handoff_enabled = true;
    
    public String[] seeds;
    public DiskAccessMode disk_access_mode = DiskAccessMode.auto;
    
    /* Address where to run the job tracker */
    public String job_tracker_host;
    
    /* Job Jar Location */
    public String job_jar_file_location;
    
    /* initial token in the ring */
    public String initial_token;
    
    public Long rpc_timeout_in_ms = new Long(2000);

    public Integer phi_convict_threshold = 8;
    
    public Integer concurrent_reads = 8;
    public Integer concurrent_writes = 32;
    
    public Integer memtable_flush_writers = null; // will get set to the length of data dirs in DatabaseDescriptor
    
    public Integer sliced_buffer_size_in_kb = 64;
    
    public Integer storage_port = 7000;
    public String listen_address;
    
    public String rpc_address;
    public Integer rpc_port = 9160;
    public Boolean rpc_keepalive = true;
    public Integer rpc_send_buff_size_in_bytes;
    public Integer rpc_recv_buff_size_in_bytes;

    public Integer thrift_max_message_length_in_mb = 16;
    public Integer thrift_framed_transport_size_in_mb = 15;
    public Boolean snapshot_before_compaction = false;
    public Integer compaction_thread_priority = Thread.MIN_PRIORITY;
    
    public Integer binary_memtable_throughput_in_mb = 256;
    
    /* if the size of columns or super-columns are more than this, indexing will kick in */
    public Integer column_index_size_in_kb = 64;
    public Integer in_memory_compaction_limit_in_mb = 256;
    
    public String[] data_file_directories;

    public String saved_caches_directory;

    // Commit Log
    public String commitlog_directory;
    public Integer commitlog_rotation_threshold_in_mb;
    public CommitLogSync commitlog_sync;
    public Double commitlog_sync_batch_window_in_ms;
    public Integer commitlog_sync_period_in_ms;
    
    public String endpoint_snitch;
    public Boolean dynamic_snitch = false;
    public Integer dynamic_snitch_update_interval_in_ms = 100;
    public Integer dynamic_snitch_reset_interval_in_ms = 600000;
    public Double dynamic_snitch_badness_threshold = 0.0;

    public String request_scheduler;
    public RequestSchedulerId request_scheduler_id;

    public Integer index_interval = 128;

    
    public static enum CommitLogSync {
        periodic,
        batch
    }
    
    public static enum DiskAccessMode {
        auto,
        mmap,
        mmap_index_only,
        standard,
    }
    
    public static enum RequestSchedulerId
    {
        keyspace
    }
}
