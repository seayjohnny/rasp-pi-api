pull.bat
	
	pscp -l [user] -pw [user_password] [user]@[device_ip_address]:[file_path_to_parent_folder]/%1 [local_filepath_to_store_file]
	
	notes: %1 represents the file name, including file extension. It is passed as an argument when running this batch file from command line.
	
	example: 