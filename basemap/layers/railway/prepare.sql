-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to you under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

CREATE MATERIALIZED VIEW osm_railway AS
SELECT id, tags, geom
FROM (
   SELECT
       min(id) as id,
       jsonb_build_object('railway', tags -> 'railway', 'service', tags -> 'service') as tags,
       (st_dump(st_linemerge(st_collect(geom)))).geom as geom
   FROM osm_ways
   WHERE tags ->> 'railway' IN ('light_rail', 'monorail', 'rail', 'subway', 'tram')
   GROUP BY tags -> 'railway', tags -> 'service'
) AS merge;
