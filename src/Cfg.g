grammar Cfg;

@header{

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.StringBuilder;

}




cfg returns[ArrayList<HashMap<String,String>> vars,ArrayList<ArrayList<String>> args ,ArrayList<ArrayList<String>> opts, ArrayList<String> preds,ArrayList<String> scopes, ArrayList<String> cmds ,int n_runs,int n_comands]
@init{
ArrayList<HashMap<String,String>> vars = new ArrayList<HashMap<String,String>>();
ArrayList<ArrayList<String>> args = new ArrayList<ArrayList<String>>();
ArrayList<ArrayList<String>> opts = new ArrayList<ArrayList<String>>();
ArrayList<String> preds = new ArrayList<String>();
ArrayList<String> scopes = new ArrayList<String>();
ArrayList<String> cmds = new ArrayList<String>();
int n_comands = 0;
}
	: a=comand{
	vars.add($a.vars);
	args.add($a.args);
	opts.add($a.opts);
	preds.add($a.pred);
	scopes.add($a.scope);
	cmds.add($a.cmd);
	n_comands++;
	}
	
	 ( ';' b=comand{
	 vars.add($b.vars);
	 args.add($b.args);
	 opts.add($b.opts);
	 preds.add($b.pred);
	 scopes.add($b.scope);
	 cmds.add($b.cmd);
	 n_comands++;
	 }
	 )* r=runs
	 {
	 	$cfg.vars = vars;
	 	$cfg.args = args;
	 	$cfg.opts = opts;
	 	$cfg.preds = preds;
	 	$cfg.scopes = scopes;
	 	$cfg.cmds = cmds;
	 	$cfg.n_runs = $r.n_runs;
	 	System.out.println(n_comands);
	 	$cfg.n_comands = n_comands;
	 }
	;	


comand   returns [HashMap<String,String> vars, ArrayList<String> args, ArrayList<String> opts, String pred,String scope, String cmd]
	: vars 'pred' pred  'scope'  sp 'cmd'  cmdgit 
	 {
	 	$comand.vars = $vars.a_vars;
		$comand.args = $pred.args;
	 	$comand.opts = $cmdgit.opts;
	  	$comand.pred = $pred.name;
	  	$comand.scope = $sp.sp;
		$comand.cmd = $cmdgit.cmd;
	
	 }
	; 

runs  returns [int n_runs]
	: 'runs' i=INT
	{
	 $runs.n_runs = Integer.parseInt($i.text);
	}
	;
	
vars returns [HashMap<String,String> a_vars]
@init{
HashMap<String,String> vars = new HashMap<String,String>();
}
	: a=var {vars.put($a.arg,$a.type);}  (b=var {vars.put($b.arg,$b.type);})*
	{ $vars.a_vars = vars;}
	;
	
pred returns [String name, ArrayList<String> args]
	: n=name a=arguments
		{
		  $pred.args = $a.args;
		  $pred.name = $n.text;
		}
	;
	

arguments returns [ArrayList<String> args]
	:'[' 's' ',' 's2' ',' g=args ']'
	{
	  $arguments.args = $g.n_ag;
	}
	;

args returns [ArrayList<String> n_ag]
@init{
ArrayList<String> args = new ArrayList<String>();
}
	:a=arg { args.add($a.ag);} (',' b=arg { args.add($b.ag);})*
	{
	 $args.n_ag = args;
	}
	;


sp  returns [String sp]
@init{
StringBuilder scope = new StringBuilder();
}
	: 'for' sigs
	{
	 scope.append("for  ");
	 for (String sig : $sigs.n_sigs)
	  {
	   scope.append(sig +" ");  	
	  }
	  $sp.sp = scope.toString();
	}
	| 'for' 'exactly' sigs
	{
	 scope.append("for exactly ");
	 for (String sig : $sigs.n_sigs)
	  {
	   scope.append(sig +" ");  	
	  }
	  $sp.sp = scope.toString();
	}
	;
	
sigs  returns [ArrayList<String> n_sigs]
@init{
ArrayList<String> sigs = new ArrayList<String>();
}
	: a=sig{ sigs.add($a.sig);} (',' b=sig { sigs.add($b.sig);} )*
	{
	 $sigs.n_sigs = sigs;
	}
	;
	
sig returns [String sig]
	:i=INT  s=ID
	{
	 $sig.sig = $i.text +" " + $s.text;
	}
	;

var returns [String arg, String type]
	: a=arg ':' t=type
	{
	 $var.arg = $a.ag;
	 $var.type = $t.text;
	}
	;
	
type 
	:ID
	;
	
cmdgit returns [String pg,String cmd,ArrayList<String> opts]
	:'git' name opts
	{
	 $cmdgit.pg = "git";
	 $cmdgit.cmd = $name.text;
	 $cmdgit.opts = $opts.n_opts;
	}
	;

opts returns [ArrayList<String> n_opts]
@init{
ArrayList<String> opts = new ArrayList<String>();
}
         :a=opt {opts.add($a.op); } (b=opt {opts.add($b.op);})*	
	{
	 $opts.n_opts = opts;
	}
	;
	
opt returns [String op]
	:'[' i= ID ']'
	{
	$opt.op = $i.text;
	}
	;
name 
	: ID
	;
	
arg returns [String ag]
	:'#'i=INT 
	{
	 $arg.ag = $i.text;
	}
	;
	
ID  :	('a'..'z'|'A'..'Z'|'_'|'-') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
    ;

INT :	'0'..'9'+
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
