# j-zerocopy
java zero copy demo

The bellow is the test result on centos 7.6 kernel 4.14

| file size | traditional(ms) | nio(ms) |
|-----------|-----------------|---------|
| 12MB      | 50              | 18      |
| 221MB     | 690             | 314     |
| 2.5G      | 15496           | 2610    |

