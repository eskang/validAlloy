#!/usr/bin/perl
   
use strict;
use File::Find;
use File::Path qw( rmtree );
use File::Spec::Functions qw( catfile );

find(\&rm_errors, $_) for @ARGV;

sub rm_errors{
    if ($_ eq "git_errors.txt"){
        print $File::Find::name,"\n";
        print $_,"\n";
        my $path =  catfile $File::Find::dir;
        print File::Spec->path(), "\n";
        print $path,"\n";
        rmtree( $path );
        exec('echo',$path);
        exec('rm','-rf',$path);
       } 
} 