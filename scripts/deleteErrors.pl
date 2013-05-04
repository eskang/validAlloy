#!/usr/bin/perl
   
use strict;
use File::Find;
use File::Path qw( rmtree );
use File::Spec::Functions qw( catfile );

my @dirs;


find(\&rm_errors, $_) for @ARGV;

for my $p (@dirs){
	rmtree($p) or die($!);
}
sub rm_errors{
    if ($_ eq "git_errors.txt"){
        print $File::Find::name,"\n";
        print $_,"\n";
        my $path =  catfile $File::Find::dir;
        print File::Spec->path(), "\n";
        print $path,"\n";
	push @dirs, $path;
       } 
} 
