source gf.config
gfsh start server --name=server1 --server-port=41122 --dir=/Users/cjohnson/Documents/workspace-sts-3.1.0.RELEASE/SpatialIndex/data/server1 --properties-file=../../gemfire.properties --max-heap=100m --initial-heap=100m --J=-XX:+UseParNewGC --J=-XX:+UseConcMarkSweepGC 
